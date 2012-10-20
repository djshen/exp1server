package Ftp;
import org.apache.ftpserver.ftplet.*;
import java.io.*;

public class MyFtplet extends DefaultFtplet
{
@Override
public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException
{
	System.err.println(session.getClientAddress().toString()+" > "+"afterCommand:"+request.getCommand());
	System.err.println("Thread #" + Thread.currentThread().getId());
	//return FtpletResult.DEFAULT;
	return super.afterCommand(session, request, reply);
}
@Override
public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException
{
	System.err.println(session.getClientAddress().toString()+" > "+"beforeCommand:"+request.getCommand());
	System.err.println("Thread #" + Thread.currentThread().getId());
	//return FtpletResult.DEFAULT;
	return super.beforeCommand(session, request);
}
@Override
public void destroy()
{
	System.err.println("destroy");
	System.err.println("Thread #" + Thread.currentThread().getId());
	super.destroy();
}
@Override
public void init(FtpletContext ftpletContext) throws FtpException
{
	System.err.println("init");
	System.err.println("Thread #" + Thread.currentThread().getId());
	super.init(ftpletContext);
}
@Override
public FtpletResult onAppendEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onAppendEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onAppendStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onAppendStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onConnect(FtpSession session) throws FtpException, IOException
{
	System.err.println(session.getClientAddress().toString()+" > "+"onConnect");
	System.err.println("Thread #" + Thread.currentThread().getId());
	//FtpReply reply = new DefaultFtpReply(FtpReply.REPLY_220_SERVICE_READY, "Welcome to my ftp server!");
	//System.err.println(reply.toString());
	//session.write(reply);
	//return FtpletResult.DEFAULT;
	return super.onConnect(session);
}
@Override
public FtpletResult onDeleteEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onDeleteEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onDeleteStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onDeleteStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onDisconnect(FtpSession session)
{
	System.err.println(session.getClientAddress().toString()+" > "+"ondisconnect");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onDownloadEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onDownloadStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onDownloadStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onLogin(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onLogin");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onMkdirEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onMkdirEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onMkdirStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onMkdirStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onRenameEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onRenameEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onRenameStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onRenameStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onRmdirEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onRmdirEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onRmdirStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onRmdirStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onSite(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onSite");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onUploadEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onUploadStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onUploadUniqueEnd(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onUploadUniqueEnd");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
@Override
public FtpletResult onUploadUniqueStart(FtpSession session, FtpRequest request)
{
	System.err.println(session.getClientAddress().toString()+" > "+"onUploadUniqueStart");
	System.err.println("Thread #" + Thread.currentThread().getId());
	return FtpletResult.DEFAULT;
}
}
