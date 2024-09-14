module ageofwar {
    requires hanyaeger;
    requires jdk.compiler;

    exports com.github.hanyaeger.tutorial;

    opens audio;
    opens backgrounds;
    opens sprites;
}
