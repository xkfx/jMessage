package chatroom.client.handler;

import chatroom.client.ui.UserInterface;
import chatroom.client.util.ClientMessageService;
import chatroom.client.ui.component.impl.UserFrame;
import chatroom.common.message.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static chatroom.client.ui.enums.ButtonEnum.SEND;
import static chatroom.common.message.Iconst.PERSONAL_MESSAGE;
import static chatroom.common.message.Iconst.PUBLIC_MESSAGE;

public class UserFrameHandler implements ActionListener {

    private ClientMessageService clientMessageService;
    private UserInterface userInterface;

    private void sendMessage() {
        final UserFrame userFrame = userInterface.getUserFrame();
        final Long source = userInterface.getSource();
        final Long target = userFrame.getTarget();
        final String content = userFrame.getInput();

        if (target == null) {
            // 群发
            Message publicMsg = new Message(PUBLIC_MESSAGE, content);
            try {
                clientMessageService.send(publicMsg);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // 发送给好友
            Message message = new Message(PERSONAL_MESSAGE, content);
            message.setSource(source);
            message.setTarget(target);
            try {
                clientMessageService.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 持有界面管理器的同时，注册相应对象的监听
     * @param manager
     */
    public void setUserInterface(UserInterface manager) {
        userInterface = manager;
        // 便于 userInterface 动态注册事件监听
        userInterface.setUserFrameHandler(this);
    }

    public void setClientMessageService(ClientMessageService clientMessageService) {
        this.clientMessageService = clientMessageService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(SEND.getExpression())) {
            sendMessage();
        }
    }
}
