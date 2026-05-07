package org.tfg.grant_java.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.ApplicationPayload;
import org.tfg.grant_java.model.VersionedItem;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final TemplateEngine templateEngine;
    private final ApplicationService applicationService;

    public byte[] exportApplicationPdf(String userId, String applicationId) {

        ApplicationPayload app =
                applicationService.getApplicationByUserAndId(userId, applicationId);

        // ✅ HERE — normalize to latest version
        normalizeToLatest(app.getFields());
        normalizeToLatest(app.getQuestions());

        Context context = new Context();
        context.setVariable("application", app);
        context.setVariable("fields", app.getFields());
        context.setVariable("questions", app.getQuestions());

        String html = templateEngine.process("application-pdf", context);

        return generatePdf(html);
    }

    private byte[] generatePdf(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private void normalizeToLatest(List<VersionedItem> items) {
        items.forEach(i -> {
            if (i.getVersions() == null || i.getVersions().isEmpty()) {
                return;
            }

            Integer latest = i.getVersions().keySet().stream()
                    .map(Integer::valueOf)
                    .max(Integer::compareTo)
                    .orElse(i.getCurrentVersion());

            i.setCurrentVersion(latest);
        });
    }
}
