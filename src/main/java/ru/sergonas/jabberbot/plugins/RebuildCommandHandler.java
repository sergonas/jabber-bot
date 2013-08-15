package ru.sergonas.jabberbot.plugins;

import ru.sergonas.jabberbot.ConfigManager;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * От этого класса несет костыльностью за километр. Переделать!
 * TODO переписать класс убрав повышенную костыльность
 * User: Сергей
 * Date: 15.08.13
 * Time: 11:42
 */
public class RebuildCommandHandler implements CommandHandler {
    private static final String COMMAND_NAME = "rebuild";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String executeCommand(String args) {
        String response;
        try {
            sendBuildSignal();
            response = "Rebuild started. Shutting down.";
        } catch (Exception e) {
            e.printStackTrace();
            response = "Something goes bad.";
        }
        return response;
    }

    private void sendBuildSignal() throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                new UsernamePasswordCredentials(ConfigManager.getParam("jenkinsUser"), ConfigManager.getParam("jenkinsApiToken")));
        BasicScheme basicAuth = new BasicScheme();
        BasicHttpContext context = new BasicHttpContext();
        context.setAttribute("preemptive-auth", basicAuth);
        client.addRequestInterceptor(new PreemptiveAuth(), 0);
        String getUrl = ConfigManager.getParam("jenkinsServer") + "jenkins" + "/job/" + ConfigManager.getParam("jenkinsJobName") + "/build?token=" + ConfigManager.getParam("jenkinsBuildToken");
        HttpGet get = new HttpGet(getUrl);
        try {
            HttpResponse response = client.execute(get, context);
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class PreemptiveAuth implements HttpRequestInterceptor {
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost
                            .getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }

        }

    }
}
