# recipe_project 2023-11-26
- 메인 페이지 출력시 favorite컬렉션 참조하여 하트 버튼 상태 설정
- 하트 버튼 클릭시 찜하기/찜하기취소 기능 구현
- 메인페이지 viewPager2 이미지 랜덤으로 출력

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/7159c7d6-c298-49bb-a890-5d748253d87a)

- 검색페이지 네잎클로버 버튼 클릭시 추천 레시피로 이동

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/36a28c2b-02af-41e7-8b85-0de94a4e355a) ![image](https://github.com/rydnjs98/recipe_project/assets/89891055/0d10e2e8-34b6-4ebe-bfe6-303452304669)




# recipe_project 2023-11-24
- favorite페이지 LikeButton 클릭시 삭제 이벤트 구현

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/7c07e312-cceb-4b87-831d-d97d9b37ac7f)

- 동영상 재생,h264으로 재생환경 변경

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/959a2b10-5567-4556-b536-0271b34373f3)

- 레시피 상세 페이지 재료 버튼 동적 생성

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/d1fa180a-591d-4728-b73a-85da86a5a1c4)

- 재료 버튼 클릭시 웹뷰 구현

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/f0224eb4-b0f8-4a24-baec-7bbac934a34b)




# recipe_project 2023-11-23
- 메인 페이지 하트 버튼 및 텍스트 추가

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/cfed3dfd-b559-4ed6-b937-504fe51b7e54)


# recipe_project 2023-11-21
- 모든 페이지 이미지 동적 생성

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/43c7b4c2-56fa-4b21-aa7b-d6fb05e510e4) ![image](https://github.com/rydnjs98/recipe_project/assets/89891055/77c86564-60db-4c8a-9019-9965432e7bd2)
![image](https://github.com/rydnjs98/recipe_project/assets/89891055/77bf7bb3-9c6c-4e8f-856e-bc531f5fdb14)

- search 페이지 이미지 클릭시 recipe페이지로 이동 후 해당 레시피의 정보 로그로 출력

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/6fa70dc6-90df-43b1-835e-c2ab5222d348)



# recipe_project 2023-11-20
- 메인 페이지 버튼들 동적 생성
  
![image](https://github.com/rydnjs98/recipe_project/assets/89891055/cf9f9455-2c8a-4886-94b9-44d7976654f1)

- Firestore 연동 동영상 재생 기능 수정

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/b102374c-c461-4d40-904c-ffd5e1410618)



# recipe_project 2023-11-16
-Firestore 재료테이블 array 번호 삽입
![image](https://github.com/vahallla/recipe_project/assets/89891803/9e3ea9ec-9a44-4983-8968-38ffe6edcf49)






# recipe_project 2023-11-15
-레시피 제목 Firestore연결

![image](https://github.com/vahallla/recipe_project/assets/89891803/0854040d-7996-4623-a303-f19cb4ae4afd) 

현재 동영상과 연결중





-Favorite 테이블 및 오류수정

![image](https://github.com/vahallla/recipe_project/assets/89891803/5d689754-dacc-452d-9795-04cd7d18a7d4)

favorite.java에서 읽어오는 방법 수정
회원가입 시 favorite 테이블에 user_id, recipe_ID 생성 가능하게 만들었고, 페이지 넘어가지 않던 오류 수정


-Search 정보수집 및 웹크롤링 추가

서치 페이지 현재 시간가져오기 + 현재 (익산)날씨 가져오기 <- 웹크롤링을 사용
레시피 테이블 태그 살짝 수정. 야식, 점심, 아침등 시간에 따라 추가. 날씨에 따라는 추가예정

구현 예정 현재 가져온 시간과 날씨를 기반으로 현재의 검색버튼을 오늘의 추천 레시피 버튼으로 변경후
클릭시 시간과 날씨를 기준으로 바로 레시피 페이지로 이동하게 만들예정








# recipe_project 2023-11-08
# 기능 구현
-Youtube Video 기능 구현

![동영상1](https://github.com/vahallla/recipe_project/assets/89891803/a4548006-aef8-41fb-9a47-6dfc0390fe1f)

https://github.com/PierfrancescoSoffritti/android-youtube-player
을 사용하여 유튜브 연결

- Search 기능 구현

![레시피_검색_1](https://github.com/vahallla/recipe_project/assets/89891803/310bbd91-94f7-4d00-8952-c9e7c6ad567f) ![레시피_검색결과(일식)](https://github.com/vahallla/recipe_project/assets/89891803/5bb871a8-70b4-436c-9047-52bd0a188948)
![레시피_찜하기_로그](https://github.com/vahallla/recipe_project/assets/89891803/32ee62a1-e058-49fb-a3ea-c7b8f5267894)



- Favorite 기능 구현

![레시피_찜하기](https://github.com/vahallla/recipe_project/assets/89891803/7c3c8da6-043a-478e-b9af-e91c4ed186b5) ![레시피_찜하기_로그](https://github.com/vahallla/recipe_project/assets/89891803/9e1fad02-987b-43b0-ab99-67326611e381)










# recipe_project 2023-11-04
# 디자인 수정
![image](https://github.com/rydnjs98/recipe_project/assets/89891055/46e5a99e-477f-4493-add3-9682d920f5d1) ![image](https://github.com/rydnjs98/recipe_project/assets/89891055/2226a65d-d4c2-46d0-8bf8-0012e4a4817f)

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/e84c34c6-5998-414d-90d4-34908e649b3a) ![image](https://github.com/rydnjs98/recipe_project/assets/89891055/3dc98005-061b-4a61-8708-5d30fadfa7ab)

![image](https://github.com/rydnjs98/recipe_project/assets/89891055/8b3130c4-a8c3-4398-b73c-4002d910482b) ![image](https://github.com/rydnjs98/recipe_project/assets/89891055/a57afabd-1d88-47a4-9403-e55ab0330870) 



# recipe_project 2023-11-01
# 코드 수정
![그림01](https://github.com/rydnjs98/recipe_project/assets/89891055/de286092-fa17-489c-910d-d5b0fb2c84f1)


비디오뷰 구현 및 메인 페이지 이미지 슬라이더 구현
