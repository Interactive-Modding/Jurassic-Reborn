package mod.reborn.client.model;

import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;

public class TabulaModelUV extends TabulaModelContainer {
    public TabulaModelUV(TabulaModelContainer container, int height, int width) {
        super(container.getName(), container.getAuthor(), width, height, container.getCubes(), container.getProjectVersion());
    }
}
