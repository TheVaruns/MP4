
public interface Communicator 
{
	public void send(StateInfo info);
	public void send(TransferInfo info);
	public void requestState();
	public void requestTransfer();
}
