using System;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Traces;

internal readonly record struct ValueTrace(
    string EventName,
    PredefinedProvider Provider,
    DateTime TimeStamp, 
    string Content
);