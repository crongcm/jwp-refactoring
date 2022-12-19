package kitchenpos.order.acceptance;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestUtils.메뉴_면류_짜장면;
import static kitchenpos.order.acceptance.OrderAcceptanceTestUtils.*;
import static kitchenpos.order.domain.OrderLineItemTestFixture.orderLineItemRequest;
import static kitchenpos.table.acceptance.TableAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("주문 인수 테스트")
class OrderAcceptanceTest extends kitchenpos.OrderAcceptanceTest {
    private OrderTableResponse 테이블;
    private OrderTableResponse 비어있는_테이블;
    private MenuResponse 메뉴_면류_짜장면;
    private OrderResponse 주문;

    @DisplayName("주문 인수 테스트")
    @TestFactory
    Stream<DynamicNode> orderAcceptance() {
        return Stream.of(
                dynamicTest("테이블과 메뉴를 생성한다.", () -> {
                    테이블 = 주문_테이블_등록되어_있음(1, false);
                    비어있는_테이블 = 주문_테이블_등록되어_있음(0, true);
                    메뉴_면류_짜장면 = 메뉴_면류_짜장면();
                }),
                dynamicTest("주문 등록시 주문 항목은 필수이다.", () -> {
                    ExtractableResponse<Response> response = 주문_생성_요청(테이블.getId(), emptyList());

                    주문_생성_실패(response);
                }),
                dynamicTest("주문 등록시 주문 항목은 모두 등록된 메뉴여야 한다.", () -> {
                    ExtractableResponse<Response> response = 주문_생성_요청(테이블.getId(),
                            singletonList(orderLineItemRequest(null, 1L)));

                    주문_생성_실패(response);
                }),
                dynamicTest("주문 등록시 주문 테이블은 등록된 테이블이어야 한다.", () -> {
                    ExtractableResponse<Response> response = 주문_생성_요청(null,
                            singletonList(
                                    orderLineItemRequest(메뉴_면류_짜장면.getId(), 1L)));

                    주문_생성_실패(response);
                }),
                dynamicTest("주문 등록시 주문 테이블은 비어있는 테이블일 수 없다.", () -> {
                    ExtractableResponse<Response> response = 주문_생성_요청(비어있는_테이블.getId(),
                            singletonList(
                                    orderLineItemRequest(메뉴_면류_짜장면.getId(), 1L)));

                    주문_생성_실패(response);
                }),
                dynamicTest("주문을 등록한다.", () -> {
                    ExtractableResponse<Response> response = 주문_생성_요청(테이블.getId(),
                            singletonList(
                                    orderLineItemRequest(메뉴_면류_짜장면.getId(), 1L)));
                    주문 = response.as(OrderResponse.class);

                    주문_생성됨(response);
                    주문_생성_주문상태_확인(response);
                }),
                dynamicTest("주문 목록을 조회하면 주문 목록이 반환된다.", () -> {
                    ExtractableResponse<Response> response = 주문_목록_조회_요청();

                    주문_목록_조회됨(response);
                    주문_목록_포함됨(response, 주문);
                }),
                dynamicTest("주문 상태를 변경하려면 주문이 등록되어야 한다.", () -> {
                    ExtractableResponse<Response> response = 주문_상태_수정_요청(Long.MAX_VALUE, OrderStatus.MEAL);

                    주문_상태_수정_실패(response);
                }),
                dynamicTest("주문의 상태를 수정한다. (조리 -> 식사)", () -> {
                    ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), OrderStatus.MEAL);

                    주문_상태_수정됨(response);
                }),
                dynamicTest("주문의 상태를 수정한다. (식사 -> 완료)", () -> {
                    ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), OrderStatus.COMPLETION);
                    ExtractableResponse<Response> response2 = 주문_테이블_목록_조회_요청();

                    주문_상태_수정됨(response);
                    주분_테이블_목록_비움여부_검증(response2, true, true);
                }),
                dynamicTest("주문 상태를 변경하려면 주문상태가 완료가 아니어야 한다.", () -> {
                    ExtractableResponse<Response> response = 주문_상태_수정_요청(주문.getId(), OrderStatus.COMPLETION);

                    주문_상태_수정_실패(response);
                })
        );
    }
}