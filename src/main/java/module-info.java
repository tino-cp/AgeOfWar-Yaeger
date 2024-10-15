module ageofwar {
    requires hanyaeger;
    requires jdk.compiler;

    exports com.github.hanyaeger.ageofwar;

    opens audio;
    opens backgrounds;
    opens sprites;
    exports com.github.hanyaeger.ageofwar.entities.troops;
    exports com.github.hanyaeger.ageofwar.entities.projectiles;
    exports com.github.hanyaeger.ageofwar.entities.buttons;
    exports com.github.hanyaeger.ageofwar.entities.texts;
    exports com.github.hanyaeger.ageofwar.utils;
    exports com.github.hanyaeger.ageofwar.entities.scenes;
    exports com.github.hanyaeger.ageofwar.entities;
    exports com.github.hanyaeger.ageofwar.entities.abilities;
}
