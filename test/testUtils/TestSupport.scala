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

package testUtils

import com.typesafe.config.Config
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Suite}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.ExecutionContext
import uk.gov.hmrc.http.HeaderCarrier

trait TestSupport extends UnitSpec with GuiceOneServerPerSuite with MockitoSugar with BeforeAndAfterAll with BeforeAndAfterEach with MaterializerSupport {
  this: Suite =>

  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  implicit val headerCarrier: HeaderCarrier = HeaderCarrier()

  implicit val config: Config = app.configuration.underlying

}
