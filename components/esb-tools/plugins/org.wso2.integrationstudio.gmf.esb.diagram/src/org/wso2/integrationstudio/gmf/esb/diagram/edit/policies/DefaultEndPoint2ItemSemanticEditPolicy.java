package org.wso2.integrationstudio.gmf.esb.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.wso2.integrationstudio.gmf.esb.diagram.edit.commands.DefaultEndPointInputConnector2CreateCommand;
import org.wso2.integrationstudio.gmf.esb.diagram.edit.commands.DefaultEndPointOutputConnector2CreateCommand;
import org.wso2.integrationstudio.gmf.esb.diagram.edit.parts.DefaultEndPointInputConnector2EditPart;
import org.wso2.integrationstudio.gmf.esb.diagram.edit.parts.DefaultEndPointOutputConnector2EditPart;
import org.wso2.integrationstudio.gmf.esb.diagram.edit.parts.EsbLinkEditPart;
import org.wso2.integrationstudio.gmf.esb.diagram.part.EsbVisualIDRegistry;
import org.wso2.integrationstudio.gmf.esb.diagram.providers.EsbElementTypes;

/**
 * @generated
 */
public class DefaultEndPoint2ItemSemanticEditPolicy extends EsbBaseItemSemanticEditPolicy {

    /**
     * @generated
     */
    public DefaultEndPoint2ItemSemanticEditPolicy() {
        super(EsbElementTypes.DefaultEndPoint_3643);
    }

    /**
     * @generated
     */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (EsbElementTypes.DefaultEndPointInputConnector_3644 == req.getElementType()) {
            return getGEFWrapper(new DefaultEndPointInputConnector2CreateCommand(req));
        }
        if (EsbElementTypes.DefaultEndPointOutputConnector_3645 == req.getElementType()) {
            return getGEFWrapper(new DefaultEndPointOutputConnector2CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }

    /**
     * @generated
     */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        View view = (View) getHost().getModel();
        CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
        cmd.setTransactionNestingEnabled(false);
        EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
        if (annotation == null) {
            // there are indirectly referenced children, need extra commands: false
            addDestroyChildNodesCommand(cmd);
            addDestroyShortcutsCommand(cmd, view);
            // delete host element
            cmd.add(new DestroyElementCommand(req));
        } else {
            cmd.add(new DeleteCommand(getEditingDomain(), view));
        }
        return getGEFWrapper(cmd.reduce());
    }

    /**
     * @generated
     */
    private void addDestroyChildNodesCommand(ICompositeCommand cmd) {
        View view = (View) getHost().getModel();
        for (Iterator<?> nit = view.getChildren().iterator(); nit.hasNext();) {
            Node node = (Node) nit.next();
            switch (EsbVisualIDRegistry.getVisualID(node)) {
            case DefaultEndPointInputConnector2EditPart.VISUAL_ID:
                for (Iterator<?> it = node.getTargetEdges().iterator(); it.hasNext();) {
                    Edge incomingLink = (Edge) it.next();
                    if (EsbVisualIDRegistry.getVisualID(incomingLink) == EsbLinkEditPart.VISUAL_ID) {
                        DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
                        cmd.add(new DestroyElementCommand(r));
                        cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
                        continue;
                    }
                }
                cmd.add(new DestroyElementCommand(
                        new DestroyElementRequest(getEditingDomain(), node.getElement(), false))); // directlyOwned: true
                // don't need explicit deletion of node as parent's view deletion would clean child views as well 
                // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), node));
                break;
            case DefaultEndPointOutputConnector2EditPart.VISUAL_ID:
                for (Iterator<?> it = node.getSourceEdges().iterator(); it.hasNext();) {
                    Edge outgoingLink = (Edge) it.next();
                    if (EsbVisualIDRegistry.getVisualID(outgoingLink) == EsbLinkEditPart.VISUAL_ID) {
                        DestroyElementRequest r = new DestroyElementRequest(outgoingLink.getElement(), false);
                        cmd.add(new DestroyElementCommand(r));
                        cmd.add(new DeleteCommand(getEditingDomain(), outgoingLink));
                        continue;
                    }
                }
                cmd.add(new DestroyElementCommand(
                        new DestroyElementRequest(getEditingDomain(), node.getElement(), false))); // directlyOwned: true
                // don't need explicit deletion of node as parent's view deletion would clean child views as well 
                // cmd.add(new org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand(getEditingDomain(), node));
                break;
            }
        }
    }

}
