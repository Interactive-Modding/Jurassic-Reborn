package mod.reborn.server.api;

import mod.reborn.server.dinosaur.Dinosaur;

public interface Hybrid {
    Class<Dinosaur>[] getDinosaurs();
}
