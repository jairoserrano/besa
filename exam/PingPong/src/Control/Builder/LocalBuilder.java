/*
 * @(#)LocalBuilder.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Builder;

import Abstraction.DEF;
import Abstraction.WorldModel;
import Abstraction.WorldModel2D;
import BESA.Kernel.Agent.AgentBESA;
import Presentation.TableTennisView;

/**
 * This class builds a local system.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class LocalBuilder extends Builder {

    /**
     * Creates a new instance.
     */
    public LocalBuilder() {
        super();
    }

    @Override
    public boolean buildGUI() {
        TableTennisView tableTennisView = new TableTennisView();
        tableTennisView.setVisible(true);
        this.game.setGUI(tableTennisView);
        return true;
    }

    @Override
    public boolean buildModel(DEF world) {
        if (world == DEF.WORLD_2D) {
            WorldModel worldModel = new WorldModel2D();
            worldModel.registerObserver(this.game.getGUI());
            this.game.setWorldModel(worldModel);
        } else {
        }
        return true;
    }

    /**
     * Builds the player A.
     *
     * @return true if the agent was created or false in an other case.
     */
    @Override
    public boolean buildPlayerA() {
        AgentBESA agent = this.createAgent(DEF.PLAYER_A.name(), this.game.getWorldModel());
        agent.start();
        return true;
    }

    /**
     * Builds the player B.
     * 
     * @return true if the agent was created or false in an other case.
     */
    @Override
    public boolean buildPlayerB() {
        AgentBESA agent = this.createAgent(DEF.PLAYER_B.name(), this.game.getWorldModel());
        agent.start();
        return true;
    }
}
