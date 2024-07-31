package com.github.coderodde.game.chess.duel;

import com.github.coderodde.game.chess.AbstractGameEngine;
import com.github.coderodde.game.chess.ChessBoardState;
import com.github.coderodde.game.chess.HeuristicFunction;
import com.github.coderodde.game.chess.PlayerTurn;
import com.github.coderodde.game.chess.ThreeFoldRepetionRuleDrawException;
import com.github.coderodde.game.chess.impl.DefaultHeuristicFunction;
import com.github.coderodde.game.chess.impl.engine.AlphaBetaPruningGameEngine;

/**
 * This class runs an AI vs. AI duel.
 * 
 * @version 1.0.0 (Jul 30, 2024)
 * @since 1.0.0 (Jul 30, 2024)
 */
public class ChessDuel {
    
    static final int MINIMUM_DEPTH = 2;

    public static void main(String[] args) {
        
        final HeuristicFunction heuristicFunction =
                new DefaultHeuristicFunction();
        
        final AbstractGameEngine engine = 
                new AlphaBetaPruningGameEngine(heuristicFunction);
        
        ChessBoardState state = new ChessBoardState();
        PlayerTurn currentPlayerTurn = PlayerTurn.WHITE;
        
        final Parameters parameters = Parameters.parseArguments(args);
        
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
            
            try {
                final int depth = currentPlayerTurn == PlayerTurn.WHITE ?
                        parameters.whitePlayerEngineDepth :
                        parameters.blackPlayerEngineDepth;
                
                final ChessBoardState previousChessBoardState = state;
                
                System.out.println("ply number: " + plyNumber);
                
                if (plyNumber == 3) {
                    System.out.println("FUNKY STATE\n" + state);
                }
                
                state = engine.search(state, 
                                      depth, 
                                      currentPlayerTurn);
                
            } catch (final ThreeFoldRepetionRuleDrawException ex) {
                System.out.println(
                        "[STATUS] Three-fold repetition rule "
                        + "broken. It's a draw!");
                
                return;
            }
            
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

class Parameters {
    int whitePlayerEngineDepth;
    int blackPlayerEngineDepth;
    
    static Parameters parseArguments(final String[] args) {
        final Parameters parameters = new Parameters();
        
        switch (args.length) {
            case 0:
                parameters.whitePlayerEngineDepth = ChessDuel.MINIMUM_DEPTH;
                parameters.blackPlayerEngineDepth = ChessDuel.MINIMUM_DEPTH;
                return parameters;
                
            case 1:
                parameters.whitePlayerEngineDepth = parseInteger(args[0]);
                parameters.blackPlayerEngineDepth = ChessDuel.MINIMUM_DEPTH;
                return parameters;
                
            default:
                parameters.whitePlayerEngineDepth = parseInteger(args[0]);
                parameters.blackPlayerEngineDepth = parseInteger(args[1]);
                return parameters;
        }
    }
    
    private static int parseInteger(final String token) {
        try {
            return Math.max(ChessDuel.MINIMUM_DEPTH, Integer.parseInt(token));
        } catch (final NumberFormatException ex) {
            return ChessDuel.MINIMUM_DEPTH;
        }
    }
}
