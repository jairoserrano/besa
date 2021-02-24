/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.model;

import presentation.model.adapter.PresentationAdapter;
import presentation.view.HiView;

/**
 *
 * @author fabianjose
 */
public class World {

    private HiView hiView;
    private PresentationAdapter presentationAdapter;
    private String message;

    public World(HiView hiView) {
        this.hiView = hiView;
        this.hiView.setWorld(this);
    }

    public void displayMessage(String message) {
        this.message = message;
        hiView.update(message);
    }

    public void sendHi() {
        this.presentationAdapter.sendHi();
    }

    public void setPresentationAdapter(PresentationAdapter presentationAdapter) {
        this.presentationAdapter = presentationAdapter;
    }


}
