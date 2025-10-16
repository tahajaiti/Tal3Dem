package org.kyojin.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

public class DebugUtil {

    private static final int MAX_DEPTH = 10;
    private static final int MAX_STRING_LENGTH = 1000;
    private static final int MAX_COLLECTION_SIZE = 100;

    public static void dd(HttpServletResponse resp, Object... objects) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        PrintWriter writer = resp.getWriter();
        writer.write(generateHtml(objects));
        writer.flush();
        writer.close();

        throw new RuntimeException("Execution stopped by dd()");
    }

    public static void dump(Object... objects) {
        System.out.println(generatePlainText(objects));
    }

    private static String generateHtml(Object... objects) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
                .append("<title>Dump & Die</title>")
                .append(getStyles())
                .append("</head><body>")
                .append("<div class='container'>")
                .append("<div class='header'>")
                .append("<span class='skull'>💀</span>")
                .append("<h1>Dump & Die</h1>")
                .append("</div>");

        if (objects == null || objects.length == 0) {
            html.append("<div class='dump-block'><span class='null'>null</span></div>");
        } else {
            IdentityHashMap<Object, Integer> visited = new IdentityHashMap<>();
            for (int i = 0; i < objects.length; i++) {
                html.append("<div class='dump-block'>");
                html.append(dumpObject(objects[i], 0, visited));
                html.append("</div>");
            }
        }

        html.append("<div class='footer'>🚫 Execution stopped</div>")
                .append("</div>")
                .append(getScript())
                .append("</body></html>");

        return html.toString();
    }

    private static String dumpObject(Object obj, int depth, IdentityHashMap<Object, Integer> visited) {
        if (obj == null) {
            return "<span class='null'>null</span>";
        }

        if (depth > MAX_DEPTH) {
            return "<span class='max-depth'>Max depth reached</span>";
        }

        if (visited.containsKey(obj)) {
            return "<span class='circular'>*CIRCULAR*</span>";
        }

        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder();

        // Primitives and primitive wrappers
        if (isPrimitiveOrWrapper(clazz)) {
            return formatPrimitive(obj, clazz);
        }

        // Strings
        if (obj instanceof String str) {
            return formatString(str);
        }

        // Enums
        if (obj instanceof Enum<?> e) {
            return "<span class='enum'>" + escapeHtml(e.name()) + "</span>";
        }

        // Dates and times
        if (obj instanceof Date || obj instanceof Temporal) {
            return "<span class='date'>\"" + escapeHtml(obj.toString()) + "\"</span>";
        }

        visited.put(obj, depth);

        // Arrays
        if (clazz.isArray()) {
            sb.append(formatArray(obj, depth, visited));
        }
        // Collections
        else if (obj instanceof Collection<?> col) {
            sb.append(formatCollection(col, depth, visited));
        }
        // Maps
        else if (obj instanceof Map<?, ?> map) {
            sb.append(formatMap(map, depth, visited));
        }
        // Objects
        else {
            sb.append(formatObject(obj, clazz, depth, visited));
        }

        visited.remove(obj);
        return sb.toString();
    }

    private static String formatPrimitive(Object obj, Class<?> clazz) {
        String type = clazz.getSimpleName().toLowerCase();
        if (obj instanceof Boolean) {
            return "<span class='boolean'>" + obj + "</span>";
        } else if (obj instanceof Number) {
            return "<span class='number'>" + obj + "</span>";
        } else if (obj instanceof Character) {
            return "<span class='string'>'" + escapeHtml(obj.toString()) + "'</span>";
        }
        return "<span class='primitive'>" + obj + "</span>";
    }

    private static String formatString(String str) {
        if (str.length() > MAX_STRING_LENGTH) {
            str = str.substring(0, MAX_STRING_LENGTH) + "...";
        }
        return "<span class='string'>\"" + escapeHtml(str) + "\"</span> <span class='length'>(length: " + str.length() + ")</span>";
    }

    private static String formatArray(Object array, int depth, IdentityHashMap<Object, Integer> visited) {
        int length = Array.getLength(array);
        StringBuilder sb = new StringBuilder();

        sb.append("<div class='collapsible-header' onclick='toggleCollapse(this)'>")
                .append("<span class='toggle'>▼</span> ")
                .append("<span class='type'>").append(array.getClass().getSimpleName()).append("</span>")
                .append(" <span class='count'>[").append(length).append("]</span>")
                .append("</div>")
                .append("<div class='collapsible-content'>");

        int limit = Math.min(length, MAX_COLLECTION_SIZE);
        for (int i = 0; i < limit; i++) {
            sb.append("<div class='item'>")
                    .append("<span class='index'>").append(i).append("</span> => ")
                    .append(dumpObject(Array.get(array, i), depth + 1, visited))
                    .append("</div>");
        }

        if (length > MAX_COLLECTION_SIZE) {
            sb.append("<div class='more'>... ").append(length - MAX_COLLECTION_SIZE).append(" more items</div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    private static String formatCollection(Collection<?> col, int depth, IdentityHashMap<Object, Integer> visited) {
        StringBuilder sb = new StringBuilder();
        int size = col.size();

        sb.append("<div class='collapsible-header' onclick='toggleCollapse(this)'>")
                .append("<span class='toggle'>▼</span> ")
                .append("<span class='type'>").append(col.getClass().getSimpleName()).append("</span>")
                .append(" <span class='count'>[").append(size).append("]</span>")
                .append("</div>")
                .append("<div class='collapsible-content'>");

        int i = 0;
        for (Object item : col) {
            if (i >= MAX_COLLECTION_SIZE) {
                sb.append("<div class='more'>... ").append(size - MAX_COLLECTION_SIZE).append(" more items</div>");
                break;
            }
            sb.append("<div class='item'>")
                    .append("<span class='index'>").append(i++).append("</span> => ")
                    .append(dumpObject(item, depth + 1, visited))
                    .append("</div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    private static String formatMap(Map<?, ?> map, int depth, IdentityHashMap<Object, Integer> visited) {
        StringBuilder sb = new StringBuilder();
        int size = map.size();

        sb.append("<div class='collapsible-header' onclick='toggleCollapse(this)'>")
                .append("<span class='toggle'>▼</span> ")
                .append("<span class='type'>").append(map.getClass().getSimpleName()).append("</span>")
                .append(" <span class='count'>{").append(size).append("}</span>")
                .append("</div>")
                .append("<div class='collapsible-content'>");

        int i = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (i++ >= MAX_COLLECTION_SIZE) {
                sb.append("<div class='more'>... ").append(size - MAX_COLLECTION_SIZE).append(" more items</div>");
                break;
            }
            sb.append("<div class='item'>")
                    .append("<span class='key'>\"").append(escapeHtml(String.valueOf(entry.getKey()))).append("\"</span> => ")
                    .append(dumpObject(entry.getValue(), depth + 1, visited))
                    .append("</div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    private static String formatObject(Object obj, Class<?> clazz, int depth, IdentityHashMap<Object, Integer> visited) {
        StringBuilder sb = new StringBuilder();
        List<Field> fields = getAllFields(clazz);

        sb.append("<div class='collapsible-header' onclick='toggleCollapse(this)'>")
                .append("<span class='toggle'>▼</span> ")
                .append("<span class='type'>").append(clazz.getSimpleName()).append("</span>")
                .append(" <span class='count'>{").append(fields.size()).append("}</span>")
                .append("</div>")
                .append("<div class='collapsible-content'>");

        for (Field field : fields) {
            field.setAccessible(true);
            String visibility = getVisibilityIcon(field);

            sb.append("<div class='item'>")
                    .append("<span class='visibility'>").append(visibility).append("</span> ")
                    .append("<span class='property'>").append(field.getName()).append("</span>: ");

            try {
                Object value = field.get(obj);
                sb.append(dumpObject(value, depth + 1, visited));
            } catch (IllegalAccessException e) {
                sb.append("<span class='error'>inaccessible</span>");
            }

            sb.append("</div>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    private static String getVisibilityIcon(Field field) {
        int mod = field.getModifiers();
        if (Modifier.isPublic(mod)) return "+";
        if (Modifier.isProtected(mod)) return "#";
        if (Modifier.isPrivate(mod)) return "-";
        return "~";
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Boolean.class || clazz == Character.class ||
                clazz == Byte.class || clazz == Short.class ||
                clazz == Integer.class || clazz == Long.class ||
                clazz == Float.class || clazz == Double.class;
    }

    private static String generatePlainText(Object... objects) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DUMP ===\n");

        if (objects == null || objects.length == 0) {
            sb.append("null\n");
        } else {
            IdentityHashMap<Object, Integer> visited = new IdentityHashMap<>();
            for (Object obj : objects) {
                sb.append(dumpObjectPlain(obj, 0, visited)).append("\n");
            }
        }

        return sb.toString();
    }

    private static String dumpObjectPlain(Object obj, int depth, IdentityHashMap<Object, Integer> visited) {
        if (obj == null) return "null";
        if (depth > MAX_DEPTH) return "...";
        if (visited.containsKey(obj)) return "*CIRCULAR*";

        String indent = "  ".repeat(depth);
        Class<?> clazz = obj.getClass();

        if (isPrimitiveOrWrapper(clazz) || obj instanceof String || obj instanceof Enum) {
            return obj.toString();
        }

        visited.put(obj, depth);
        StringBuilder sb = new StringBuilder();

        if (clazz.isArray()) {
            int len = Array.getLength(obj);
            sb.append("Array[").append(len).append("] [\n");
            for (int i = 0; i < Math.min(len, MAX_COLLECTION_SIZE); i++) {
                sb.append(indent).append("  ").append(i).append(" => ")
                        .append(dumpObjectPlain(Array.get(obj, i), depth + 1, visited)).append("\n");
            }
            sb.append(indent).append("]");
        } else if (obj instanceof Collection<?> col) {
            sb.append(clazz.getSimpleName()).append("[").append(col.size()).append("] [\n");
            int i = 0;
            for (Object item : col) {
                if (i++ >= MAX_COLLECTION_SIZE) break;
                sb.append(indent).append("  ").append(dumpObjectPlain(item, depth + 1, visited)).append("\n");
            }
            sb.append(indent).append("]");
        } else if (obj instanceof Map<?, ?> map) {
            sb.append(clazz.getSimpleName()).append("[").append(map.size()).append("] {\n");
            int i = 0;
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (i++ >= MAX_COLLECTION_SIZE) break;
                sb.append(indent).append("  ").append(e.getKey()).append(" => ")
                        .append(dumpObjectPlain(e.getValue(), depth + 1, visited)).append("\n");
            }
            sb.append(indent).append("}");
        } else {
            sb.append(clazz.getSimpleName()).append(" {\n");
            for (Field f : getAllFields(clazz)) {
                f.setAccessible(true);
                try {
                    sb.append(indent).append("  ").append(f.getName()).append(": ")
                            .append(dumpObjectPlain(f.get(obj), depth + 1, visited)).append("\n");
                } catch (IllegalAccessException e) {
                    sb.append(indent).append("  ").append(f.getName()).append(": [inaccessible]\n");
                }
            }
            sb.append(indent).append("}");
        }

        visited.remove(obj);
        return sb.toString();
    }

    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String getStyles() {
        return """
            <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { 
                background: #0d1117; 
                color: #c9d1d9; 
                font-family: 'SF Mono', Monaco, 'Cascadia Code', 'Roboto Mono', Consolas, monospace;
                font-size: 14px;
                line-height: 1.6;
                padding: 20px;
            }
            .container { max-width: 1400px; margin: 0 auto; }
            .header {
                background: linear-gradient(135deg, #1f6feb 0%, #9e4cff 100%);
                padding: 20px 30px;
                border-radius: 12px;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 15px;
                box-shadow: 0 8px 24px rgba(31, 111, 235, 0.2);
            }
            .skull { font-size: 32px; }
            h1 { 
                font-size: 28px; 
                font-weight: 700; 
                color: #ffffff;
                letter-spacing: -0.5px;
            }
            .dump-block {
                background: #161b22;
                border: 1px solid #30363d;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 16px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
            }
            .collapsible-header {
                cursor: pointer;
                user-select: none;
                padding: 8px 12px;
                background: #21262d;
                border-radius: 6px;
                margin-bottom: 8px;
                transition: background 0.2s;
                display: flex;
                align-items: center;
                gap: 8px;
            }
            .collapsible-header:hover { background: #2d333b; }
            .collapsible-content { 
                padding-left: 20px; 
                margin-left: 8px;
                border-left: 2px solid #30363d;
            }
            .collapsible-content.collapsed { display: none; }
            .toggle {
                color: #8b949e;
                font-size: 12px;
                transition: transform 0.2s;
                display: inline-block;
            }
            .collapsed .toggle { transform: rotate(-90deg); }
            .item {
                padding: 6px 12px;
                margin: 4px 0;
                border-radius: 4px;
                transition: background 0.15s;
            }
            .item:hover { background: #21262d; }
            .type { color: #79c0ff; font-weight: 600; }
            .key { color: #ffa657; font-weight: 500; }
            .property { color: #79c0ff; }
            .string { color: #a5d6ff; }
            .number { color: #79c0ff; }
            .boolean { color: #ff7b72; font-weight: 600; }
            .null { color: #8b949e; font-style: italic; }
            .enum { color: #d2a8ff; font-weight: 600; }
            .date { color: #7ee787; }
            .index { 
                color: #8b949e; 
                font-size: 12px;
                min-width: 30px;
                display: inline-block;
            }
            .count {
                color: #8b949e;
                font-size: 12px;
                font-weight: 400;
            }
            .length {
                color: #6e7681;
                font-size: 11px;
                font-style: italic;
            }
            .visibility {
                color: #8b949e;
                font-weight: 700;
                font-size: 12px;
            }
            .circular { color: #f85149; font-weight: 600; }
            .max-depth { color: #f85149; font-style: italic; }
            .error { color: #f85149; }
            .more {
                color: #8b949e;
                font-style: italic;
                padding: 6px 12px;
                margin: 4px 0;
            }
            .footer {
                background: #21262d;
                border: 1px solid #30363d;
                padding: 15px 20px;
                border-radius: 8px;
                text-align: center;
                color: #f85149;
                font-weight: 600;
                margin-top: 20px;
            }
            </style>
            """;
    }

    private static String getScript() {
        return """
            <script>
            function toggleCollapse(header) {
                const content = header.nextElementSibling;
                content.classList.toggle('collapsed');
                header.classList.toggle('collapsed');
            }
            </script>
            """;
    }
}