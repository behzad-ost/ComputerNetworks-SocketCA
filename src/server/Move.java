package server;

import common.Coordinate;
import server.client.Clients;

import java.util.List;
import java.util.TimerTask;

public class Move extends TimerTask {
    @Override
    public void run() {
        List<Coordinate> removedElements = Clients.getInstance().moveAllClients();
        Clients.getInstance().sendNewDataToAllClients(removedElements);
    }
}
