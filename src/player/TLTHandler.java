/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;

import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

public class TLTHandler implements DropTargetListener {

    private final folderSelector f;

    public TLTHandler(JPanel pane, folderSelector f) {
        this.f = f;
        this.pane = pane;

        // Create the DropTarget and register
        // it with the JPanel.
        dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY,
                this, true, null);
    }

    // Implementation of the DropTargetListener interface
    public void dragEnter(DropTargetDragEvent dtde) {

        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);
        System.out.println("We got drag with type=");
        // Accept or reject the drag.
        boolean acceptOrRejectDrag = acceptOrRejectDrag(dtde);

    }

    public void dragExit(DropTargetEvent dte) {

    }

    public void dragOver(DropTargetDragEvent dtde) {

    }

    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    public void drop(DropTargetDropEvent dtde) {

        // Check the drop action
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            // Accept the drop and get the transfer data
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();

            try {
                boolean result = dropComponent(transferable);

                dtde.dropComplete(result);

            } catch (Exception e) {

                dtde.dropComplete(false);
            }
        } else {

            dtde.rejectDrop();
        }
    }

    // Internal methods start here
    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde) {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = false;
        List<File> files = null;
        // Reject if the object being transferred
        // or the operations available are not acceptable.
        if (!acceptableType
                || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {

            dtde.rejectDrag();
        } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            // Not offering copy or move - suggest a copy

            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            acceptedDrag = true;
        } else {
            // Offering an acceptable operation: accept
            System.out.println("We got an acceptable drag");
            try {

                files = (List<File>) dtde.getTransferable().getTransferData(targetFlavor);
            } catch (UnsupportedFlavorException ex) {

            } catch (IOException ex) {

            }
            dtde.acceptDrag(dropAction);
            acceptedDrag = true;
        }
        f.initDrop(files);
        return acceptedDrag;
    }

    protected void checkTransferType(DropTargetDragEvent dtde) {
        // Only accept a flavor that returns a Component
        acceptableType = false;
        DataFlavor[] fl = dtde.getCurrentDataFlavors();

        for (int i = 0; i < fl.length; i++) {
            System.out.println(fl[i].getHumanPresentableName() + "     " + fl[i].getMimeType());
            if (fl[i].getHumanPresentableName().equals("application/x-java-file-list")) {
                targetFlavor = fl[i];
                acceptableType = true;
                break;
            }

        }

    }

    protected boolean dropComponent(Transferable transferable)
            throws IOException, UnsupportedFlavorException {
        Object o = transferable.getTransferData(targetFlavor);
        if (o instanceof Component) {

            pane.add((Component) o);
            pane.validate();
            return true;
        }
        return false;
    }

    protected JPanel pane;

    protected DropTarget dropTarget;

    protected boolean acceptableType; // Indicates whether data is acceptable

    protected DataFlavor targetFlavor; // Flavor to use for transfer
}
