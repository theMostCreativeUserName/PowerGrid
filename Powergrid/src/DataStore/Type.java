package DataStore;

public enum Type {
    Coal(Resource.Coal),
    Oil(Resource.Oil),
    Garbage(Resource.Garbage),
    Uranium(Resource.Uranium),
    /**
     * Kann Kohle und Oel verbrennen.
     * Einzelheiten setzt die Spiellogik um.
     */
    Hybrid,
    /** Braucht keine Rohstoffe. */
    Eco,
    /** Braucht keine Rohstoffe. */
    Fusion;

    /**
     * Rohstoff, den dieser Kraftwerkstyp braucht.
     * null, wenn es keinen oder mehrere gibt.
     */
    private final Resource resource;

    Type(Resource resource) {
        this.resource = resource;
    }

    Type() {
        this(null);
    }

    /**
     * Rohstoff, den das Krwaftwerk als Einzigen verwendet.
     * @return Rohstoff, den dieser Kraftwerkstyp braucht.
     * null, wenn es keinen bestimmten gibt.
     */
    public Resource getResource() {
        return resource;
    }

}
