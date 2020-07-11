package net.minecraftforge.eventbus.benchmarks;

import cpw.mods.modlauncher.ClassTransformer;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.TransformStore;
import cpw.mods.modlauncher.TransformingClassLoader;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.service.ModLauncherService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.powermock.reflect.Whitebox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.uncheck;

@State(Scope.Benchmark)
public class EventBusBenchmark
{
    private IEventBus dynamicSubscriberBus;
    private IEventBus lambdaSubscriberBus;
    private IEventBus staticSubscriberBus;
    private Supplier<Event> cancableConstructor;
    private Supplier<Event> resultConstructor;
    private Supplier<Event> simpleConstructor;

    @SuppressWarnings("unchecked")
    @Setup
    public void setup()
    {
        String packageName = "net/minecraftforge/eventbus/benchmarks/compiled/";
        String[] toTransform = new String[]{"CancelableEvent", "ResultEvent", "SimpleEvent", "SubscriberDynamic", "SubscriberLambda", "SubscriberStatic"};
        Class<?>[] classes = new Class[toTransform.length];

        //Setup class transformers
        final TransformStore transformStore = new TransformStore();
        final LaunchPluginHandler lph = new LaunchPluginHandler();
        ClassTransformer classTransformer = uncheck(()->Whitebox.invokeConstructor(ClassTransformer.class, new Class[] { transformStore.getClass(),  lph.getClass(), TransformingClassLoader.class }, new Object[] { transformStore, lph, null}));
        Method transform = Whitebox.getMethod(classTransformer.getClass(), "transform", byte[].class, String.class);
        LaunchPluginHandler pluginHandler = Whitebox.getInternalState(classTransformer, "pluginHandler");
        Map<String, ILaunchPluginService> plugins = Whitebox.getInternalState(pluginHandler, "plugins");
        ModLauncherService service = new ModLauncherService();
        plugins.put(service.name(), service); //Inject it

        //Setup class loader injects
        Method defineClass = Whitebox.getMethod(ClassLoader.class, "defineClass", String.class, byte[].class, int.class, int.class);
        //Setup event and subscriber classes
        for (int i = 0; i < toTransform.length; i++)
        {
            String className = toTransform[i];
            className = packageName + className;
            byte[] classBytes;
            try (InputStream is = getClass().getResourceAsStream("/" + className + ".class"))
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1];
                while (is.read(buf) >= 0)
                {
                    bos.write(buf);
                }
                classBytes = bos.toByteArray();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            String clsWithDot = className.replace('/', '.');
            try
            {
                classBytes = (byte[]) transform.invoke(classTransformer, classBytes, clsWithDot);
                classes[i] = (Class) defineClass.invoke(getClass().getClassLoader(), clsWithDot, classBytes, 0, classBytes.length);
            } catch (ReflectiveOperationException e)
            {
                throw new RuntimeException(e);
            }
        }
        try
        {
            cancableConstructor = (Supplier<Event>) classes[0].getDeclaredField("makeNew").get(null);
            resultConstructor = (Supplier<Event>) classes[1].getDeclaredField("makeNew").get(null);
            simpleConstructor = (Supplier<Event>) classes[2].getDeclaredField("makeNew").get(null);
        } catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }

        //Setup bus and Subscriber
        dynamicSubscriberBus = BusBuilder.builder().build();
        lambdaSubscriberBus = BusBuilder.builder().build();
        staticSubscriberBus = BusBuilder.builder().build();
        try
        {
            dynamicSubscriberBus.register(Whitebox.invokeConstructor(classes[3]));
            Whitebox.invokeMethod(classes[4], "register", lambdaSubscriberBus);
            staticSubscriberBus.register(classes[5]);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public int testDynamic() throws Exception
    {
        postAll(dynamicSubscriberBus);
        return 0;
    }

    @Benchmark
    public int testLambda() throws Exception
    {
        postAll(lambdaSubscriberBus);
        return 0;
    }

    @Benchmark
    public int testStatic() throws Exception
    {
        postAll(staticSubscriberBus);
        return 0;
    }

    public void postAll(IEventBus bus)
    {
        bus.post(cancableConstructor.get());
        bus.post(resultConstructor.get());
        bus.post(simpleConstructor.get());
    }

}
