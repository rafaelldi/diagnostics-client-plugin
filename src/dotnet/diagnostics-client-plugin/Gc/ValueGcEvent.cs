namespace DiagnosticsClientPlugin.Gc;

internal readonly record struct ValueGcEvent(
    int Number,
    int Generation
);