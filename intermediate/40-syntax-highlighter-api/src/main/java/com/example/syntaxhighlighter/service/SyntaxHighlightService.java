package com.example.syntaxhighlighter.service;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Token;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class SyntaxHighlightService {

    public String highlight(String code, String language) {
        RSyntaxTextArea textArea = new RSyntaxTextArea(code);
        String syntaxStyle = getSyntaxStyle(language);
        textArea.setSyntaxEditingStyle(syntaxStyle);
        
        // Ensure we are in a headless mode mostly, but RSyntaxTextArea is a Swing component.
        // We can iterate over tokens to generate HTML manually to avoid Swing rendering dependencies if needed,
        // or we can use the library's built-in export if available.
        // RSyntaxTextArea doesn't have a direct "toHtml" method that returns a string easily without some setup.
        // However, we can use the Token system.

        StringBuilder html = new StringBuilder();
        html.append("<pre style='font-family:monospace; background-color: #ffffff; color: #000000;'>");

        Token token = textArea.getTokenListForLine(0);
        int lineCount = textArea.getLineCount();
        
        for (int i = 0; i < lineCount; i++) {
            token = textArea.getTokenListForLine(i);
            while (token != null && token.isPaintable()) {
                appendTokenAsHtml(html, token);
                token = token.getNextToken();
            }
            html.append("\n");
        }
        
        html.append("</pre>");
        return html.toString();
    }

    private void appendTokenAsHtml(StringBuilder html, Token token) {
        // Simple mapping based on token type for demonstration
        // In a real scenario, we might want to map RSyntaxTextArea styles to CSS classes or inline styles.
        // Here we will use simple inline styles for common types.
        
        String style = "";
        switch (token.getType()) {
            case Token.RESERVED_WORD:
                style = "color: #000080; font-weight: bold;";
                break;
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
            case Token.LITERAL_CHAR:
                style = "color: #008000;";
                break;
            case Token.COMMENT_EOL:
            case Token.COMMENT_MULTILINE:
            case Token.COMMENT_DOCUMENTATION:
                style = "color: #808080; font-style: italic;";
                break;
            case Token.FUNCTION:
                style = "color: #000000;";
                break;
            case Token.DATA_TYPE:
                style = "color: #20b2aa;"; // Light Sea Green
                break;
            case Token.ANNOTATION:
                style = "color: #808000;";
                break;
            default:
                style = "color: #000000;";
                break;
        }

        html.append("<span style='").append(style).append("'>");
        html.append(escapeHtml(token.getLexeme()));
        html.append("</span>");
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private String getSyntaxStyle(String language) {
        if (language == null) return SyntaxConstants.SYNTAX_STYLE_NONE;
        
        switch (language.toLowerCase()) {
            case "java": return SyntaxConstants.SYNTAX_STYLE_JAVA;
            case "python": return SyntaxConstants.SYNTAX_STYLE_PYTHON;
            case "xml": return SyntaxConstants.SYNTAX_STYLE_XML;
            case "json": return SyntaxConstants.SYNTAX_STYLE_JSON;
            case "javascript":
            case "js": return SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
            case "c": return SyntaxConstants.SYNTAX_STYLE_C;
            case "cpp":
            case "c++": return SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
            case "html": return SyntaxConstants.SYNTAX_STYLE_HTML;
            case "css": return SyntaxConstants.SYNTAX_STYLE_CSS;
            case "sql": return SyntaxConstants.SYNTAX_STYLE_SQL;
            case "bash":
            case "sh": return SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL;
            default: return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }
}
