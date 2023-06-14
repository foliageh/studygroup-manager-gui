package com.labproject.handlers;

import com.labproject.exceptions.ServerException;
import com.labproject.models.User;
import com.labproject.network.CollectionRequest;
import com.labproject.network.CollectionResponse;
import com.labproject.services.UserService;
import com.labproject.views.*;

import java.net.Socket;
import java.util.HashMap;

public class CollectionRequestHandler extends RequestHandler {
    private final HashMap<String, View> views = new HashMap<>() {{
        put("login", new LoginView());
        put("register", new RegisterView());
        put("info", new InfoView());
        put("show", new ShowView());
        put("add", new AddView());
        put("update", new UpdateView());
        put("remove_by_id", new RemoveByIdView());
        put("remove_greater", new RemoveGreaterView());
        put("remove_lower", new RemoveLowerView());
    }};
    private final UserService userService = UserService.getInstance();

    public CollectionRequestHandler(Socket socket) {
        super(socket);
    }

    /** Генерирует ответ на запрос через View, соответствующий команде из запроса.
     * Если учетные данные неверны или не были предоставлены, возвращает ответ с ошибкой,
     * иначе устанавливает id пользователю-отправителю запроса, для корректной обработки View. */
    protected CollectionResponse createResponse(CollectionRequest request) {
        View view = views.get(request.getCommand());
        if (view == null)
            return new CollectionResponse(false, "msg.unknownCommand");
        if (request.getUser() == null)
            return new CollectionResponse(false, "msg.authError");
        if (!request.getCommand().equals("login") && !request.getCommand().equals("register")) {
            try {
                User user = userService.login(request.getUser());
                request.setUser(user);
            } catch (ServerException e) {
                return new CollectionResponse(false, "msg.badCredentials");
            }
        }
        return view.generateResponse(request);
    }
}
