/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.transferable;

import hr.algebra.bll.blModels.ActorModel;
import hr.algebra.bll.blModels.DirectorModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
//SEEMS THAT FOR TWO DIFFERENT MODELS FOR DND ON THE SAME VIEW/PAGE I NEED TO SEPARETE THE FLAVORS

/**
 *
 * @author antev
 */
public class ItemsTransferable implements Transferable {

    public static final DataFlavor ACTOR_FLAVOR = new DataFlavor(ActorModel.class, "ACTModel");
    public static final DataFlavor DIRECTOR_FLAVOR = new DataFlavor(DirectorModel.class, "DIRModel");
    private static final DataFlavor[] SUPPORTED_FLAVORS = {ACTOR_FLAVOR, DIRECTOR_FLAVOR};

    private final Object object;

    public ItemsTransferable(Object object) {
        if (!(object instanceof ActorModel) && !(object instanceof DirectorModel)) {
            throw new IllegalArgumentException("Unsupported object type");
        }
        this.object = object;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ACTOR_FLAVOR) || flavor.equals(DIRECTOR_FLAVOR);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(ACTOR_FLAVOR) && object instanceof ActorModel) {
            return (ActorModel) object;
        }
        if (flavor.equals(DIRECTOR_FLAVOR) && object instanceof DirectorModel) {
            return (DirectorModel) object;
        }
        throw new UnsupportedFlavorException(flavor);
    }

}
