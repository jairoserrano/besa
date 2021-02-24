/*
 * @(#)TableTennisAView.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Presentation;

import Abstraction.Ball;
import Abstraction.DEF;
import BESA.Log.ReportBESA;
import javax.swing.ImageIcon;

/**
 * This class represents GUI.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class TableTennisAView extends javax.swing.JFrame implements GUIObserver {

    private javax.swing.JLabel jLball;
    private javax.swing.JLabel jLplayer;
    private javax.swing.JPanel jPanel1;

    /** 
     * Creates new form TableTennisBView
     */
    public TableTennisAView() {
        init();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(getClass().getResource("/Presentation/lbesa.gif"));
        setIconImage(icon.getImage());
    }

    private void init() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);


        jPanel1 = new Table("/Presentation/tableA.png");
        jPanel1.setLayout(null);
        jPanel1.setBackground(new java.awt.Color(102, 153, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel1.setBounds(0, 0, 305, 390);

        jLball = new javax.swing.JLabel();
        jLball.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentation/ball.gif"))); // NOI18N
        jLball.setBounds(10, 10, 20, 20);
        jPanel1.add(jLball);

        jLplayer = new javax.swing.JLabel();
        jLplayer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Presentation/agentA.gif"))); // NOI18N
        jLplayer.setBounds(211, 250, 80, 80);
        jPanel1.add(jLplayer);

        add(jPanel1);

        setSize(311, 423);
    }

    public synchronized void distributedChangueModel(Ball ball, double[] playerPV) {

        /*if(ball.isVisible()){
        jLball.setIcon(ballIcon);
        } else {
        jLball.setIcon(nballIcon);
        }*/

        ReportBESA.debug("[BALL]: X: " + ball.getPV()[0] + " Y: " + ball.getPV()[1]);
        jLball.setLocation((int) ball.getPV()[0], (int) ball.getPV()[1]);
        jLplayer.setLocation((int) playerPV[0], (int) playerPV[1]);
        jLball.setVisible(ball.isVisible());

        //ReportBESA.debug("[---]: X: " + jLball.getX() + " Y: " + jLball.getX());      
    }

    public void localChangueModel(Ball ball, DEF player, double[] playerPV) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
