import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.Server;
import service.Service;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        var server = new Server(new Service(new MemoryGameDAO(), new MemoryUserDAO(), new MemoryAuthDAO()));
        server.run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}