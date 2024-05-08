package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TeitasBot implements BotServiceProvider {
    private List<TrucoCard> myCards;
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard secondBestCard;
    private TrucoCard worstCard;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        boolean HasNutsHand = TeitasBotFunctions.hasNutsHand(cards,vira);



        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        //choose card to first round and second implementation

        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        TrucoCard bestCard = null;
        TrucoCard secondBestCard = null;
        TrucoCard worstCard = null;


        /*
        if(TeitasBotFunctions.firstToPlay(intel)) {
            return secondBestCard;
        }
        */
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        final int maoDeOnze = 11;
        final int maxHandPoints = 12;

        List<GameIntel.RoundResult> roundsAteAgora = intel.getRoundResults();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        Boolean myHandNuts = TeitasBotFunctions.hasNutsHand(cards,vira);
        Boolean myHandGood = TeitasBotFunctions.hasGoodHand(cards,vira);
        Boolean myHandStrong= TeitasBotFunctions.hasStrongHand(cards,vira);
        Boolean myHandsTrash= TeitasBotFunctions.hasTrashHand(cards,vira);

        if (intel.getScore() == maoDeOnze ||
                intel.getOpponentScore() == maoDeOnze ||
                intel.getHandPoints() == maxHandPoints) {
            return -1;
        }

        if (roundsAteAgora.isEmpty()) {

            if (myHandGood || myHandStrong) {
                //bota pra forcar e foda se.
                return 1;

            } else if (myHandNuts) {
                return 0;
            }
        }

        if(roundsAteAgora.get(0).equals(GameIntel.RoundResult.WON)){
            if (myHandNuts || myHandStrong) {
                return 1;
            }
            else if (myHandGood) {
                return 1;
            }
            else
                return -1;
        }

        if(roundsAteAgora.get(0).equals(GameIntel.RoundResult.DREW)){
            //FODA SE
            return 1;
        }

        if(roundsAteAgora.get(0).equals(GameIntel.RoundResult.LOST)) {
            if(myHandStrong | myHandNuts){
                return 1;
            }
            else if (myHandGood) {
                return 0;
            }
            else
                return -1;
        }
        return 0;
    }
}
