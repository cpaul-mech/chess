import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.Server;
import service.GameService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var server = new Server(); //this will call the default values!!
        server.run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}