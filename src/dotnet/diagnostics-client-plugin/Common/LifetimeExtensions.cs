using System;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Common;

internal static class LifetimeExtensions
{
    internal static Lifetime IntersectWithTimer(this Lifetime lifetime, int? seconds) =>
        seconds.HasValue
            ? lifetime.CreateTerminatedAfter(TimeSpan.FromSeconds(seconds.Value))
            : lifetime;

    internal static Lifetime IntersectWithTimer(this Lifetime lifetime, Lifetime other, int? seconds)
    {
        var intersected = lifetime.Intersect(other);
        return seconds.HasValue
            ? intersected.CreateTerminatedAfter(TimeSpan.FromSeconds(seconds.Value))
            : intersected;
    }
}