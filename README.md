#Steward
######Steward는 목표를 향해 날아갈수있도록 도와주는 당신만의 APP입니다.
<br>
##미션1 - 카카오톡 알림커버를 따라해봅시다
#####잠금화면? 그까이거 껌이지
>1. 160723 : 잠금화면 추가 및 리시버 추가. 컨트롤러 클래스 추가
>2. 160724 : 잠금화면 중복 제거 및 잠금화면 유무를 프리퍼런스 사용
>3. 160726 : ScreenController를 서비스로 바꿈(프리퍼런스 제거). 도즈모드 테스트 결과 정상작동
>4. 160727 : 실시간 현재시간 반영(RX). 슬라이드 애니메이션 추가(가중치 및 취소 미적용)
>5. 160728 : ISSUE발생. 홈버튼 제어를 위해 기존의 activity기반의 잠금화면을 view로 변경해야함
>6. 160729 : activity -> view(TYPE_SYSTEM_ERROR) / DISABLE KEYGUARD는 미적용
>7. 160731 : DISABLE KEYGUARD 적용(WindowManager의 AddView의 경우 1개만 가능하여 1px 투명 액티비티로 해제)
>#####헥헥.. 생각보다 keyguardlock API가 deprecated되면서 우회해서 구현하자니 너무나 힘들다 ㅠㅠ

----------------------------------------------------------------------------------

#ToDo-List
###미션2 - 자동 데이터동기화를 구현해봅시다