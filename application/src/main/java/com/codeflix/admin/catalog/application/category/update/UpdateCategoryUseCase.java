package com.codeflix.admin.catalog.application.category.update;

import com.codeflix.admin.catalog.application.UseCase;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
