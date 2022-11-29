namespace DiagnosticsClientPlugin.Common;

internal static class DiagnosticSourceFilterAndPayloadSpecs
{
    internal const string FilterAndPayloadSpecs = "FilterAndPayloadSpecs";

    internal static readonly string[] Http =
    {
        "HttpHandlerDiagnosticListener/System.Net.Http.HttpRequestOut.Start@Activity2Start:-Request.RequestUri;Request.Method;Request.RequestUri.Host;Request.RequestUri.Port",
        "HttpHandlerDiagnosticListener/System.Net.Http.HttpRequestOut.Stop@Activity2Stop:-ActivityId=*Activity.Id;ActivityDuration=*Activity.Duration.Ticks"
    };

    internal static readonly string[] AspNetCore =
    {
        "Microsoft.AspNetCore/Microsoft.AspNetCore.Hosting.HttpRequestIn.Start@Activity1Start:-Request.Scheme;Request.Host;Request.PathBase;Request.QueryString;Request.Path;Request.Method",
        "Microsoft.AspNetCore/Microsoft.AspNetCore.Hosting.HttpRequestIn.Stop@Activity1Stop:-ActivityId=*Activity.Id;Request.Path;Response.StatusCode;ActivityDuration=*Activity.Duration.Ticks"
    };

    internal static readonly string[] EntityFrameworkCore =
    {
        "Microsoft.EntityFrameworkCore/Microsoft.EntityFrameworkCore.Database.Command.CommandExecuting@Activity2Start:-Command.Connection.DataSource;Command.Connection.Database;Command.CommandText",
        "Microsoft.EntityFrameworkCore/Microsoft.EntityFrameworkCore.Database.Command.CommandExecuted@Activity2Stop:-"
    };
}