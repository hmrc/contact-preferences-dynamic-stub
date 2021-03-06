/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import javax.inject.{Inject, Singleton}
import models.SchemaModel
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent}
import repositories.SchemaRepository
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import utils.MongoSugar

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SetupSchemaController @Inject()(schemaRepository: SchemaRepository) extends BaseController with MongoSugar {

  val addSchema: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SchemaModel] { json =>
      insert(schemaRepository)(json)
    }
  }

  val removeSchema: String => Action[AnyContent] = schemaId => Action.async { implicit request =>
    remove(schemaRepository)("_id" -> schemaId)
  }

  val removeAllSchemas: Action[AnyContent] = Action.async { implicit request =>
    removeAll(schemaRepository)
  }
}
