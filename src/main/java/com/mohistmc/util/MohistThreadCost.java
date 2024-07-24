package com.mohistmc.util;

import com.mohistmc.MohistMC;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MohistThreadCost {
    static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    {
        threadMXBean.setThreadCpuTimeEnabled(true);
    }

    public static void dumpThreadCpuTime() {
        List<ThreadCpuTime> list = new ArrayList<>();
        long[] ids = threadMXBean.getAllThreadIds();
        for (long id : ids) {
            ThreadCpuTime item = new ThreadCpuTime();
            item.cpuTime = threadMXBean.getThreadCpuTime(id) / 1000000;
            item.userTime = threadMXBean.getThreadUserTime(id) / 1000000;
            item.name = threadMXBean.getThreadInfo(id).getThreadName();
            item.id = id;
            list.add(item);
        }
        list.sort(Comparator.comparingLong(i -> i.id));
        MohistMC.LOGGER.info(MohistMC.i18n.as("mohist.dump.1"));
        for (ThreadCpuTime threadCpuTime : list) {
            MohistMC.LOGGER.info(String.format("%s %s %s %s", threadCpuTime.id, threadCpuTime.name, threadCpuTime.cpuTime, threadCpuTime.userTime));
        }
    }

    public static class ThreadCpuTime {
        private long id, cpuTime, userTime;
        private String name;
    }
}
