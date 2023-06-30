# 재활용_어디로

## 1. 목표와 기능
### 1.1 목표
- 사진을 통해 품복을 분류해주어 검색 과정 단순화
- 검색하기 애매한 품목 쉽게 탐지 및 검색
- 컵 종류에 따라 서로 다른 어플을 설치해야하는 번거로움 해소
- 브랜드별 서로 다른 보증금 반환 방법을 안내

### 1.2 기능
- 요일과 함께 상시 배출 가능한 쓰레기 품목 안내
- 이미지를 통해 검출한 품목의 배출 방법을 안내
- 매장 위치 및 컵보증금 반환 방법 안내

## 2. 개발 환경 및 배포 URL
### 2.1 개발 환경
- Android Studio
- Prototype
  - Figma
- DataBase System
  - Firebase
- Deep Learning
  - PARSEC
  - SELECTSTAR
  - Yolov5
  - PyTorch
  - CUDA/ cuDNN
  - NETRON
- Team project
  - GitHub, GitHub Projects

### 2.2 배포 URL
- https://dlqovop.github.io/ (임시)

## 3. 개발 일정
![image](https://github.com/dLqovop/capstone/assets/126761271/26e31ebb-7ef8-4713-bdf2-ba0d7189169b)

## 4. 역할 분담
- 팀장 : 문지현 (안드로이드스튜디오 개발_ 이미지검출, 딥러닝 학습, 모델 추출 및 변환, 데이터셋 변환)
- 팀원
  - 정ㅇㅇ : UI 디자인 및 구현
  - 현ㅇㅇ : 안드로이드스튜디오 개발_컵보증금페이지, 자료관리
  - 정ㅇㅇ : 안드로이드스튜디오 개발_데이터베이스, 데이터베이스관리, 데이터셋 변환

## 5. UI / BM
### 5.1 PROTOTYPE
![image](https://github.com/dLqovop/capstone/assets/126761271/4c6d219e-5dd2-429b-9019-b1e30cee34e8)

### 5.2
- 전체 페이지 구조

![image](https://github.com/dLqovop/capstone/assets/126761271/1d25b07a-8825-4de3-977c-e9786fe26bed)

###   
![image](https://github.com/dLqovop/capstone/assets/126761271/37ece2ee-b115-40ee-a1a6-792733a7b809)
- 메인 페이지
- 요일과 함께 상시 배출 가능 쓰레기 품목 안내

![image](https://github.com/dLqovop/capstone/assets/126761271/be15dd8d-8898-4fa5-b4b8-7d86c50fb409)
- 이미지 검출 페이지
- paper(종이), pack(종이류),can(캔, 철류), glass(유리), pet(페트), plastic(플라스틱), vinyl(비닐) 로 품목 구분
- 갤러리에서 선택한 이미지 또는 카메라로 찍은 이미지를 검출하여 품목 감지
- PyTorch 환경에서 yolov5 학습 모델로 학습한 결과를 변환 및 추출(*.torchscript.ptl)하여 안드로이드스튜디오에 연동
- 이미지 → 텐서로 변환 → 읽어들인 모델로 객체 탐지 → 결과값을 텐서로 변환 → 텐서에서 float형 배열로 변환 한 결과를 PrePostProcessor클래스에서 앞서 변환한 배열에서 각각의 객체 좌표를 추출 
- 바운딩 박스가 겹친 객체와 아닌 객체를 구분하고 이에 맞추어 품목 배출 방법을 안내

![image](https://github.com/dLqovop/capstone/assets/126761271/71a769d8-4d90-4515-bf0c-74d2ad2ab543)
- 일회용컵, 다회용컵 두 종류를 기준으로 배출 가능 매장 위치(제주 시청 주변) 안내
- 브랜드 별 컵 보증금 반환 방법 안내

## 6. 개선방향
- 안드로이드와 데이터베이스 기능 및 UI 개선
- 이미지 검출 기능 개선
- 컵보증금 안내 매장 범위 확장
- 분리수거 및 보증금 반환 방법 안내 내용 개선
- 홈 화면 기능 개선
- 클린하우스 위치 안내 기능 추가

## 7. 개발하면서 느낀 점
- 문지현 : darknet, darkflow 로 빌드한 환경에서 학습하다가 처음으로 PyTorch 환경에서 빌드해보고, 

