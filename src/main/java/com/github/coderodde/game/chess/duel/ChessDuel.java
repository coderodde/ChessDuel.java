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
        
        System.out.println(state);
        
        while (true) {
            System.out.println("PLY");
            
            if (state.isCheckMate(currentPlayerTurn)) {
                System.out.println("[STATUS] White player won!");
                return;
            }
            
            final long computationStartTime = System.currentTimeMillis();
            
            state = engine.search(state, 
                                  currentDepth, 
                                  currentPlayerTurn);
            
            final long computationEndTime = System.currentTimeMillis();
            final long computationDuration = computationEndTime - 
                                             computationStartTime;
            
            if (computationDuration < 1000) {
                currentDepth++;
            } else if (computationDuration > 5000) {
                currentDepth = Math.max(1, currentDepth - 1);
            }
            
            currentPlayerTurn = flipCurrentPlayerTurn(currentPlayerTurn);
            
            System.out.println(state);
        }
    }
    
    private static PlayerTurn 
        flipCurrentPlayerTurn(final PlayerTurn currentPlayerTurn) {
        return currentPlayerTurn == PlayerTurn.WHITE ?
                PlayerTurn.BLACK :
                PlayerTurn.WHITE;
    }
}
