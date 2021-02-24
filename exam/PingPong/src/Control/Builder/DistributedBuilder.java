/*
 * @(#)DistributedBuilder.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Builder;

import Abstraction.DEF;
import Abstraction.WorldModel;
import Abstraction.WorldModel2D;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.System.AdmBESA;
import Presentation.TableTennisAView;
import Presentation.TableTennisBView;

/**
 * This class builds a distributed system.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class DistributedBuilder extends Builder {

    /**
     *
     */
    private String PLAYER;

    @Override
    public boolean buildGUI() {
        String aliasAdm = AdmBESA.getInstance().getAdmHandler().getAlias();
        if(aliasAdm.equalsIgnoreCase("MAS_1")){
            PLAYER = DEF.PLAYER_A.name();
        } else {
            PLAYER = DEF.PLAYER_B.name();
        }       
        if (PLAYER.equalsIgnoreCase(DEF.PLAYER_A.name())) {
            TableTennisAView tableTennisAView = new TableTennisAView();
            tableTennisAView.setVisible(true);
            this.game.setGUI(tableTennisAView);
            return true;
        }
        if (PLAYER.equalsIgnoreCase(DEF.PLAYER_B.name())) {
            TableTennisBView tableTennisBView = new TableTennisBView();
            tableTennisBView.setVisible(true);
            this.game.setGUI(tableTennisBView);
            return true;
        }
        return false;
    }

    @Override
    public boolean buildModel(DEF world) {
        if (world == DEF.WORLD_2D) {
            WorldModel worldModel = new WorldModel2D();
            worldModel.registerObserver(this.game.getGUI());
            worldModel.setPLAYER(PLAYER);
            this.game.setWorldModel(worldModel);
        } else {
        }
        return true;
    }

    @Override
    public boolean buildPlayerA() {
        if (PLAYER.equalsIgnoreCase(DEF.PLAYER_A.name())) {
            AgentBESA agent = this.createAgent(DEF.PLAYER_A.name(), this.game.getWorldModel());
            this.game.setPLAYER(PLAYER);
            agent.start();
        }
        return true;
    }

    @Override
    public boolean buildPlayerB() {
        if (PLAYER.equalsIgnoreCase(DEF.PLAYER_B.name())) {
            AgentBESA agent = this.createAgent(DEF.PLAYER_B.name(), this.game.getWorldModel());
            this.game.setPLAYER(PLAYER);
            agent.start();
        }
        return true;
    }
}
