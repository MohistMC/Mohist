package red.mohist.extra;

import net.minecraft.entity.ai.goal.Goal;

public interface ExtraExtendedGoal {
    Goal.Flag[] getRequiredControls();
}
