package modules;

import chess.ChessGame;

public record GameData(String gameID, String whiteUsername, String blackUsername, String GameName, ChessGame game) {
}