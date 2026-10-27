 List<Integer>ans=list.stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        System.out.println(ans);
        System.out.println("Try clicking the Run button.");
        List<Integer>ans=list.stream().filter(x->x%2!=0).map(x->x*x).collect(Collectors.toList());
        System.out.println(ans);