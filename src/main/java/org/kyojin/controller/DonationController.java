package org.kyojin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kyojin.core.Injector;
import org.kyojin.core.annotation.Route;
import org.kyojin.dto.request.DonationRequestDTO;
import org.kyojin.dto.response.DonationResponseDTO;
import org.kyojin.service.DonationService;

import java.io.IOException;

@Route("/donate")
public class DonationController extends Controller {

    private DonationService donationService;

    @Override
    public void init() {
        donationService = Injector.get(DonationService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String receiverIdStr = param(req, "receiverId");
        if (receiverIdStr == null || receiverIdStr.isEmpty()) {
            renderError(req, resp, 400, "Bad Request: Missing receiver ID");
            return;
        }

        try {
            Long receiverId = Long.parseLong(receiverIdStr);

            DonationRequestDTO requestDTO = new DonationRequestDTO();
            requestDTO.setReceiverId(receiverId);

            DonationResponseDTO response = donationService.makeDonation(requestDTO, req);

            if (response.success()) {
                setSuccess(req, response.message());
            } else {
                setError(req, response.message());
            }

            resp.sendRedirect(req.getContextPath() + "/receiver");

        } catch (NumberFormatException e) {
            renderError(req, resp, 400, "Invalid receiver ID");
        } catch (Exception e) {
            renderError(req, resp, 500, "Internal Server Error: " + e.getMessage());
        }
    }
}
