/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.azmicro.moms.util.impression;

/**
 *
 * @author Aissa
 */
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.properties.UnitValue;

public class CustomLineDrawer implements ILineDrawer {
    private final float lineWidth;
    private Color lineColor;


    public CustomLineDrawer(float lineWidth, Color lineColor) {
        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
    }

    @Override
    public void draw(PdfCanvas pdfCanvas, Rectangle rect) {
        pdfCanvas.setLineWidth(lineWidth);
        pdfCanvas.setStrokeColor(lineColor);
        pdfCanvas.moveTo(rect.getLeft(), rect.getBottom());
        pdfCanvas.lineTo(rect.getRight(), rect.getBottom());
        pdfCanvas.stroke();
    }

    // Implémenter getLineWidth pour renvoyer la largeur de la ligne
    
    @Override
    public float getLineWidth() {
        return lineWidth;
    }

    @Override
    public void setLineWidth(float lineWidth) {
        lineWidth = lineWidth;
    }

    @Override
    public Color getColor() {
        return lineColor;
    }

    @Override
    public void setColor(Color color) {
        lineColor = color;
    }
}

