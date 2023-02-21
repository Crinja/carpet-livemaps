package carpet_livemaps;

import carpet.api.settings.Validators;
import carpet.api.settings.Rule;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.CREATIVE;

public class LiveMapsSettings {

    @Rule(
            options = {"1", "20", "200"},
            validators = {Validators.NonNegativeNumber.class, CheckMapTickValue.class},
            categories = {CREATIVE, "livemaps"},
            strict = false
    )
    public static int mapTicks = 20;

    @Rule(
            options = {"Disabled", "Ticks", "RedstonePower"},
            categories = {CREATIVE, "livemaps"}
    )
    public static String liveMapsFunction = "Disabled";

    private static class CheckMapTickValue extends Validator<Integer>
    {
        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            return newValue <= 1200 ? newValue : mapTicks;
        }
    }

}

