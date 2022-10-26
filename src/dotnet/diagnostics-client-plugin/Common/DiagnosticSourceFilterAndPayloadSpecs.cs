namespace DiagnosticsClientPlugin.Common;

internal static class DiagnosticSourceFilterAndPayloadSpecs
{
    internal const string FilterAndPayloadSpecs = "FilterAndPayloadSpecs";

    internal const string Http =
        "HttpHandlerDiagnosticListener/System.Net.Http.HttpRequestOut.Start@Activity2Start:-" +
        "Request.RequestUri;" +
        "Request.Method;" +
        "Request.RequestUri.Host;" +
        "Request.RequestUri.Port" +
        "\r\n" +
        "HttpHandlerDiagnosticListener/System.Net.Http.HttpRequestOut.Stop@Activity2Stop:-" +
        "ActivityId=*Activity.Id;" +
        "ActivityDuration=*Activity.Duration.Ticks";

    internal const string AspNetCore =
        "Microsoft.AspNetCore/Microsoft.AspNetCore.Hosting.HttpRequestIn.Start@Activity1Start:-" +
        "Request.Scheme;" +
        "Request.Host;" +
        "Request.PathBase;" +
        "Request.QueryString;" +
        "Request.Path;" +
        "Request.Method" +
        "\r\n" +
        "Microsoft.AspNetCore/Microsoft.AspNetCore.Hosting.HttpRequestIn.Stop@Activity1Stop:-" +
        "ActivityId=*Activity.Id;" +
        "Request.Path;" +
        "Response.StatusCode;" +
        "ActivityDuration=*Activity.Duration.Ticks";

    internal const string EntityFrameworkCore =
        "Microsoft.EntityFrameworkCore/Microsoft.EntityFrameworkCore.Database.Command.CommandExecuting@Activity2Start:-" +
        "Command.Connection.DataSource;" +
        "Command.Connection.Database;" +
        "Command.CommandText" +
        "\r\n" +
        "Microsoft.EntityFrameworkCore/Microsoft.EntityFrameworkCore.Database.Command.CommandExecuted@Activity2Stop:-";
}