package tuvarna.ticket_center_server.data;

public class EventStatistic {

    int totalTickets;

    int soldTickets;

    public EventStatistic(int totalTickets, int soldTickets) {
        this.totalTickets = totalTickets;
        this.soldTickets = soldTickets;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }
}
