module order.app {
    requires order.core;
    requires order.storage;
    requires order.threading;
    requires order.processing;

    requires java.sql;
    requires javafaker;
    requires org.apache.commons.lang3;
    requires snakeyaml;
    requires generex;
    requires automaton;
}
