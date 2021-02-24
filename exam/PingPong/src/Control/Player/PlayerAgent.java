/*
 * @(#)PlayerAgent.java 2.0	11/01/11
 *
 * Copyright 2011, Pontificia Universidad Javeriana, All rights reserved.
 * Takina and SIDRe PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package Control.Player;

import Abstraction.DEF;
import Abstraction.GameRandom;
import BESA.Kernel.Agent.AgentBESA;
import BESA.Kernel.Agent.KernelAgentExceptionBESA;
import BESA.Kernel.Agent.StateBESA;
import BESA.Kernel.Agent.StructBESA;
import BESA.Log.ReportBESA;
import Control.Player.Data.PlayerState;

/**
 * This class represents to player agent.
 *
 * @author  SIDRe - Pontificia Universidad Javeriana
 * @author  Takina  - Pontificia Universidad Javeriana
 * @version 2.0, 11/01/11
 * @since   JDK1.0
 */
public class PlayerAgent extends AgentBESA {

    /**
     * Creates an instance.
     *
     * @param alias Player alias.
     * @param state Player state.
     * @param structAgent Player structure.
     * @param passwd Player password.
     */
    public PlayerAgent(String alias, StateBESA state, StructBESA structAgent, double passwd) throws KernelAgentExceptionBESA {
        super(alias, state, structAgent, passwd);
    }

    /**
     * Setup agent.
     */
    @Override
    public void setupAgent() {
        ReportBESA.debug("Setup agent: " + this.getAlias());
        PlayerState playerState = (PlayerState) this.getState();
        playerState.setProfile(GameRandom.createPlayerProfile());
        if (this.getAlias().equalsIgnoreCase(DEF.PLAYER_A.name())) {
            playerState.getPPV()[0] = DEF.XA_POSITION.getValue();
            playerState.getPPV()[1] = DEF.YA_POSITION.getValue();
        } else {
            if (this.getAdmLocal().isCentralized()) {
                playerState.getPPV()[0] = DEF.XB_POSITION.getValue();
                playerState.getPPV()[1] = DEF.YB_POSITION.getValue();
            } else {
                playerState.getPPV()[0] = 221;
                playerState.getPPV()[1] = 251;
            }
        }
    }

    /**
     * Shutdown agent.
     */
    @Override
    public void shutdownAgent() {
    }
}
