/*
 * @(#)TableTennisAView.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Presentation;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class represents GUI.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class Table extends JPanel {

    /**
     *
     */
    private Image imagen;

    /**
     *
     * @param path
     */
    public Table(String path) {
        if (path != null) {
            imagen = new ImageIcon(getClass().getResource(path)).getImage();
        }
    }

    /**
     * 
     * @param nombreImagen
     */
    public void setImagen(String nombreImagen) {
        if (nombreImagen != null) {
            imagen = new ImageIcon(
                    getClass().getResource(nombreImagen)).getImage();
        } else {
            imagen = null;
        }
        repaint();
    }

    /**
     *
     * @param nuevaImagen
     */
    public void setImagen(Image nuevaImagen) {
        imagen = nuevaImagen;
        repaint();
    }

    /**
     * 
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            setOpaque(false);
        } else {
            setOpaque(true);
        }
        super.paint(g);
    }
}
