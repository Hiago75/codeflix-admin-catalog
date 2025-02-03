package com.codeflix.admin.catalog.domain.exceptions;

import com.codeflix.admin.catalog.domain.validation.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
