package cc.uraniummc.eventexecutor;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.spigotmc.CustomTimingsHandler;

public abstract class EventExecutorImp implements EventExecutor{

    protected CustomTimingsHandler mTimings=null;
    protected Class<?> mEventClass=null;

    @Override
    public final void execute(Listener pListener,Event pEvent) throws EventException{
        if(this.mEventClass==null||!this.mEventClass.isAssignableFrom(pEvent.getClass())){
            return;
        }

        boolean isAsync=pEvent.isAsynchronous()&&this.mTimings!=null;;
        if(!isAsync) this.mTimings.startTiming();
        try{
            this.invoke(pListener,pEvent);
        }catch(Throwable e){
            throw new EventException(e);
        }
        if(!isAsync) this.mTimings.stopTiming();
    }

    public void initExecute(Class<?> pEventClass,CustomTimingsHandler pTimings){
        this.mEventClass=pEventClass;
        this.mTimings=pTimings;
    }

    public abstract void invoke(Listener pListener,Event pEvent) throws Throwable;

}
