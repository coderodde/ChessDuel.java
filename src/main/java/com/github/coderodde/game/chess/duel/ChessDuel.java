package com.github.coderodde.game.chess.duel;

import com.github.coderodde.game.chess.AbstractGameEngine;
import com.github.coderodde.game.chess.ChessBoardState;
import com.github.coderodde.game.chess.AbstractHeuristicFunction;
import com.github.coderodde.game.chess.PlayerTurn;
import com.github.coderodde.game.chess.ThreeFoldRepetionRuleDrawException;
import com.github.coderodde.game.chess.impl.ShannonHeuristicFunction;
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
        
        final AbstractHeuristicFunction heuristicFunction =
                new ShannonHeuristicFunction();
        
        final AbstractGameEngine engine = 
                new AlphaBetaPruningGameEngine(heuristicFunction);
        
        ChessBoardState state = new ChessBoardState();

        System.out.println(state);
        
        final Parameters parameters = Parameters.parseArguments(args);
        
        parameters.whitePlayerEngineDepth = 3;
        parameters.blackPlayerEngineDepth = 2;
        
        long totalDurationMillisWhite = 0L;
        long totalDurationMillisBlack = 0L;
        
        System.out.printf("[INFO] White player engine depth: %d.\n",
                          parameters.whitePlayerEngineDepth);
        
        System.out.printf("[INFO] Black player engine depth: %d.\n", 
                          parameters.blackPlayerEngineDepth);
        
        int plyNumber = 1;
        
        while (true) {
            
            System.out.printf("[STATUS] Ply number %d by white player bot:\n", 
                              plyNumber++);
            
            long computationStartTimeMillis = System.currentTimeMillis();
            
            try {
                state = engine.search(state, 
                                      parameters.whitePlayerEngineDepth,
                                      PlayerTurn.WHITE);
                
            } catch (final ThreeFoldRepetionRuleDrawException ex) {
                System.out.println(
                        "[STATUS] Three-fold repetition rule "
                        + "broken. It's a draw!");
                
                break;
            }
            
            System.out.println(state);
            
            long computationEndTimeMillis = System.currentTimeMillis();
            long computationTimeMillis = computationEndTimeMillis 
                                       - computationStartTimeMillis;
            
            totalDurationMillisWhite += computationTimeMillis;
            
            System.out.printf("[STATUS] White AI bot ply in %d milliseconds.\n",
                              computationTimeMillis);
            
            if (state.isCheckMate(PlayerTurn.BLACK)) {
                System.out.println("[STATUS] White player won!");
                break;
            }
            
            System.out.printf("[STATUS] Ply number %d by black player bot:\n",
                              plyNumber++);
            
            computationStartTimeMillis = System.currentTimeMillis();
            
            try {
                state = engine.search(state,
                                      parameters.blackPlayerEngineDepth, 
                                      PlayerTurn.BLACK);
                
            } catch (final ThreeFoldRepetionRuleDrawException ex) {
                System.out.println(
                        "[STATUS] Three-fold repetition rule "
                        + "broken. It's a draw!");
                
                break;
            }
            
            System.out.println(state);
            
            computationEndTimeMillis = System.currentTimeMillis();
            computationTimeMillis = computationEndTimeMillis 
                                  - computationStartTimeMillis;
            
            totalDurationMillisBlack += computationTimeMillis;
            
            System.out.printf("[STATUS] Black AI bot ply in %d milliseconds.\n",
                              computationTimeMillis);
            
            if (state.isCheckMate(PlayerTurn.WHITE)) {
                System.out.println("[STATUS] Black player won!");
                break;
            }
        }
        
        System.out.printf("White AI bot total duration: %d milliseconds.\n",
                          totalDurationMillisWhite);
        
        System.out.printf("Black AI bot total duration: %d milliseconds.\n",
                          totalDurationMillisBlack);
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
