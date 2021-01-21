import org.w3c.dom.Element;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BookcaseValidator extends Remote
{
    public int getCountPageInBook(Element book) throws RemoteException;
}