/*
 * Copyright 2017 HM Revenue & Customs
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

package repositories

import javax.inject.{Inject, Singleton}

import models.{RouteExceptionKeyModel, RouteExceptionModel}
import play.modules.reactivemongo.MongoDbConnection
import reactivemongo.api.commands._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RouteExceptionRepository @Inject()() extends MongoDbConnection {

  lazy val repository = new RouteExceptionRepositoryBase {

    override def findAllVersionsBy(key: RouteExceptionKeyModel)
                                  (implicit ec: ExecutionContext): Future[Map[RouteExceptionKeyModel, List[RouteExceptionModel]]] = {
      find("id" -> key.id, "url" -> key.url).map {
        exceptions =>
          exceptions.groupBy(ex => RouteExceptionKeyModel(ex.id, ex.routeId))
      }
    }

    override def findLatestVersionBy(key: RouteExceptionKeyModel)(implicit ec: ExecutionContext): Future[List[RouteExceptionModel]] = {
      findAllVersionsBy(key).map {
        _.values.toList.map {
          _.head
        }
      }
    }

    override def removeBy(criteria: RouteExceptionKeyModel)(implicit ec: ExecutionContext): Future[Unit] = {
      remove("id" -> criteria.id).map { _ => }
    }

    override def removeAll()(implicit ec: ExecutionContext): Future[Unit] = {
      removeAll(WriteConcern.Acknowledged).map { _ => }
    }

    override def addEntry(document: RouteExceptionModel)(implicit ec: ExecutionContext): Future[Unit] = {
      insert(document).map { _ => }
    }

    override def addEntries(entries: Seq[RouteExceptionModel])(implicit ec: ExecutionContext): Future[Unit] = {
      entries.foreach {
        addEntry
      }
      Future.successful({})
    }
  }

  def apply(): CgtRepository[RouteExceptionModel, RouteExceptionKeyModel] = repository
}
