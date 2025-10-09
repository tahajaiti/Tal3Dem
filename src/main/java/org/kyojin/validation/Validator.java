package org.kyojin.validation;

import org.kyojin.core.annotation.Injectable;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@Injectable
public class Validator {

    private final Logger logger = Logger.getLogger(Validator.class.getName());
    private final Map<String, ValidationRule> validators = new HashMap<>();

    public Validator() {
        init();
        logger.info("Loaded validators: " + validators.keySet());
    }

    public Builder check(Object value, String fieldName) {
        return new Builder(value, fieldName, validators);
    }

    public static class Builder {
        private final Object value;
        private final String fieldName;
        private final Map<String, ValidationRule> rules;

        public Builder(Object value, String fieldName, Map<String, ValidationRule> rules) {
            this.value = value;
            this.fieldName = fieldName;
            this.rules = rules;
        }

        public ValidationResult rules(String rules) {
            String[] rulesArr = rules.split("\\|");

            for (String rule: rulesArr) {
                String ruleName = rule.toLowerCase();
                String params = Arrays.toString(new String[0]);

                if (rule.contains(":")) {
                    String[] parts = rule.split(":", 2);
                    ruleName = parts[0].toLowerCase();
                    params = parts[1];
                }

                ValidationRule ruleClass = this.rules.get(ruleName);
                if (ruleClass == null) {
                    return ValidationResult.fail("Validation rule not found: " + ruleName);
                }

                boolean passed = ruleClass.check(value, params);
                if (!passed) {
                    return ValidationResult.fail(ruleClass.message(fieldName, params));
                }

            }

            return ValidationResult.pass();
        }

    }

    private void init() {
        URL folderPath = Thread.currentThread().getContextClassLoader()
                .getResource("org/kyojin/validation/rule");
        if (folderPath == null) throw new IllegalArgumentException("Package not found: org.kyojin.validation.rule");

        File dir = new File(folderPath.getFile());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().endsWith(".class")) {
                String className = "org.kyojin.validation.rule." + file.getName().replace(".class", "");
                try {
                    Class<?> cls = Class.forName(className);
                    if (ValidationRule.class.isAssignableFrom(cls)) {
                        ValidationRule instance = (ValidationRule) cls.getDeclaredConstructor().newInstance();
                        String key = cls.getSimpleName().replace("Rule", "").toLowerCase();

                        validators.put(key, instance);
                    }
                } catch (Exception e) {
                    logger.warning("Failed to load validation rule: " + className + " due to " + e.getMessage());
                }
            }
        }
    }

}
