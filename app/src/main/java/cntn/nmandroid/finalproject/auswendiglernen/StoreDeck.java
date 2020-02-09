package cntn.nmandroid.finalproject.auswendiglernen;

public class StoreDeck {
    private String id;
    private String name;
    private int cardCount;
    private long createdAt;

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    private void computeTimestamp() {
        this.createdAt = (long)1e15 - System.currentTimeMillis();
    }

    public StoreDeck() {
        computeTimestamp();
    }

    public StoreDeck(String id, String name, int cardCount) {
        this.id = id;
        this.name = name;
        this.cardCount = cardCount;
        computeTimestamp();
    }

    public StoreDeck(Deck deck) {
        this.id = deck.id;
        this.name = deck.name;
        this.cardCount = deck.generateCardList().size();
        computeTimestamp();
    }
}
