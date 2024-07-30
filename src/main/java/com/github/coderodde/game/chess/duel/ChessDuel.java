package com.github.coderodde.game.chess.duel;

import com.github.coderodde.game.chess.AbstractGameEngine;
import com.github.coderodde.game.chess.ChessBoardState;
import com.github.coderodde.game.chess.HeuristicFunction;
import com.github.coderodde.game.chess.PlayerTurn;
import com.github.coderodde.game.chess.impl.DefaultHeuristicFunction;
import com.github.coderodde.game.chess.impl.engine.AlphaBetaPruningGameEngine;

/**
 * This class runs an AI vs. AI duel.
 * 
 * @version 1.0.0 (Jul 30, 2024)
 * @since 1.0.0 (Jul 30, 2024)
 */
public class ChessDuel {

    public static void main(String[] args) {
        
        int currentDepth = 3;
        
        final HeuristicFunction heuristicFunction =
                new DefaultHeuristicFunction();
        
        final AbstractGameEngine engine = 
                new AlphaBetaPruningGameEngine(heuristicFunction);
        
        ChessBoardState state = new ChessBoardState();
        PlayerTurn currentPlayerTurn = PlayerTurn.WHITE;
        
        int plyNumber = 1;
        
        while (true) {
            System.out.printf("[STATUS] Ply number: %d.\n", plyNumber++);
            
            System.out.println(state);
            
            if (state.isCheckMate(currentPlayerTurn)) {
                final String playerColorName = 
                        getPlayerColorName(currentPlayerTurn);
                
                System.out.printf("[STATUS] %s player won!\n", playerColorName);
                return;
            }
            
            final long computationStartTime = System.currentTimeMillis();
            
            state = engine.search(state, 
                                  currentDepth, 
                                  currentPlayerTurn);
            
            if (state == null) {
                throw new IllegalStateException("state == null");
            }
            
            final long computationEndTime = System.currentTimeMillis();
            final long computationDuration = computationEndTime - 
                                             computationStartTime;
            
//            if (computationDuration < 500) {
//                currentDepth++;
//            } else if (computationDuration > 5000) {
//                currentDepth = Math.max(2, currentDepth - 1);
//            }
            
            System.out.printf("%s player ply in %d milliseconds.\n",
                              getPlayerColorName(currentPlayerTurn),
                              computationDuration);
            
            currentPlayerTurn = flipCurrentPlayerTurn(currentPlayerTurn);
        }
    }
    
    private static PlayerTurn 
        flipCurrentPlayerTurn(final PlayerTurn currentPlayerTurn) {
        return currentPlayerTurn == PlayerTurn.WHITE ?
                PlayerTurn.BLACK :
                PlayerTurn.WHITE;
    }
        
    private static String 
        getPlayerColorName(final PlayerTurn currentPlayerTurn) {
        if (currentPlayerTurn == PlayerTurn.WHITE) {
            return "White";
        } else if (currentPlayerTurn == PlayerTurn.BLACK) {
            return "Black";
        } else {
            throw new IllegalStateException("Should not get here.");
        }
    }
}
